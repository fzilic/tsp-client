/**
 * Copyright (c) 2014, Franjo Žilić All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *
 */

package com.github.fzilic.tsp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;


import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampResponse;
import org.bouncycastle.tsp.TimeStampToken;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.github.fzilic.tsp.config.Algorithm;
import com.github.fzilic.tsp.config.Configuration;
import com.github.fzilic.tsp.config.PkiStatus;
import com.github.fzilic.tsp.config.Verbosity;
import com.github.fzilic.tsp.exceptions.DigestException;
import com.github.fzilic.tsp.exceptions.ExceptionHandler;
import com.github.fzilic.tsp.exceptions.Rfc3161Exception;
import com.github.fzilic.tsp.utils.PkcsOidHelper;

public class TspClientRunner {

    private static Configuration CONFIGURATION = new Configuration();

    // TODO Logback
    public static void main(final String[] p_arguments) throws IOException, NoSuchAlgorithmException, IllegalStateException, TSPException {
        Thread.currentThread().setUncaughtExceptionHandler(new ExceptionHandler());

        final CmdLineParser parser = new CmdLineParser(CONFIGURATION);

        try {
            parser.parseArgument(p_arguments);
        } catch (final CmdLineException e) {
            System.out.println(e.getMessage());
            parser.printUsage(System.out);
            return;
        }

        if (CONFIGURATION.isHelpAsked()) {
            parser.printUsage(System.out);
            return;
        }

        final DigestGenerator digestGenerator = new DigestGenerator(CONFIGURATION.getData(), CONFIGURATION.getAlgorithm());

        digestGenerator.initDigest().initFile().generateDigest();

        if (CONFIGURATION.isVerbose()) {
            System.out.println(String.format("Digest algorithm: %s%n", CONFIGURATION.getAlgorithm().getDigestAlgorithm()));
            if (Verbosity.V2.equals(CONFIGURATION.getVerbosity())) {
                System.out.println(String.format("Digest:%n%s%n", new String(Hex.encodeHex(digestGenerator.digest()))));
            }
        }

        if (CONFIGURATION.getDigestFile() != null) {
            try {
                digestGenerator.storeDigest(CONFIGURATION.getDigestFile());
            } catch (final DigestException exception) {
                System.out.println(exception.getMessage());
            }
        }

        final RequestGenerator requestGenerator = new RequestGenerator(CONFIGURATION.getAlgorithm(), digestGenerator.digest()).certReq(CONFIGURATION.isCertReq());

        final TimeStampRequest timeStampRequest = requestGenerator.request();

        if (CONFIGURATION.isVerbose()) {
            System.out.println(String.format("RFC3161 request messageImprint.hashAlgorithm: %s%n", Algorithm.hashAlgorithmForASN1ObjectIdentifier(timeStampRequest.getMessageImprintAlgOID())));
            if (Verbosity.V2.equals(CONFIGURATION.getVerbosity())) {
                System.out.println(String.format("RFC3161 request ASN1 representation:%n%s%n", ASN1Dump.dumpAsString(new ASN1InputStream(timeStampRequest.getEncoded()).readObject(), true)));
            }
        }

        if (CONFIGURATION.getQueryFile() != null) {
            try {
                requestGenerator.storeRequest(CONFIGURATION.getQueryFile());
            } catch (final Rfc3161Exception exception) {
                System.out.println(exception.getMessage());
            }
        }

        if (CONFIGURATION.getUrl() == null) {
            return;
        }

        final HttpTimestampClient client = new HttpTimestampClient(CONFIGURATION.getUrl(), timeStampRequest);

        client.initClient().execute();

        if (client.httpStatus() != HttpStatus.SC_OK) {
            System.out.println(String.format("Failed to read RFC3161 response from url, got HTTP status: %d", client.httpStatus()));
            return;
        }

        final TimeStampResponse timestampResponse = client.timestampResponse();

        if (!PkiStatus.isGranted(timestampResponse.getStatus())) {
            System.out.println(String.format("RFC3161 token not granted with status: %s - %s", PkiStatus.forStatus(timestampResponse.getStatus()), timestampResponse.getStatusString()));
        }

        if (CONFIGURATION.isVerbose()) {
            if (PkiStatus.isGranted(timestampResponse.getStatus())) {
                System.out.println(String.format("RFC3161 response messageImprint.hashAlgorithm: %s%n",
                        Algorithm.hashAlgorithmForASN1ObjectIdentifier(timestampResponse.getTimeStampToken().getTimeStampInfo().getMessageImprintAlgOID())));
                try {
                    final Field tsaSignerInfoField = TimeStampToken.class.getDeclaredField("tsaSignerInfo");
                    tsaSignerInfoField.setAccessible(true);
                    final SignerInformation tsaSignerInfo = (SignerInformation) tsaSignerInfoField.get(timestampResponse.getTimeStampToken());

                    System.out.println(String.format("RFC3161 response %n\tSignerInfo.digestAlgorithm: %s", Algorithm.hashAlgorithmForASN1ObjectIdentifier(tsaSignerInfo.getDigestAlgOID())));

                    final String algName = PkcsOidHelper.nameForOid(tsaSignerInfo.getEncryptionAlgOID());
                    if (algName != null) {
                        System.out.println(String.format("\tSignerInfo.encryptionAlgorithm: %s%n", algName));
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            if (Verbosity.V2.equals(CONFIGURATION.getVerbosity())) {
                System.out.println(String.format("RFC3161 response ASN1 representation:%n%s%n", ASN1Dump.dumpAsString(new ASN1InputStream(timestampResponse.getEncoded()).readObject(), true)));
            }
        }

        if (CONFIGURATION.getResponseFile() != null) {
            final File responseFile = new File(CONFIGURATION.getResponseFile());
            try {
                final OutputStream output = new FileOutputStream(responseFile);
                IOUtils.write(timestampResponse.getEncoded(), output);
                output.close();
            } catch (final IOException exception) {
                throw new Rfc3161Exception(exception.getMessage(), exception);
            }
        }
    }
}
