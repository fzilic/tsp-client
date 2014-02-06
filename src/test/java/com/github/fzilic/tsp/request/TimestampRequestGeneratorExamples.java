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
package com.github.fzilic.tsp.request;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.tsp.TSPAlgorithms;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampRequestGenerator;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class TimestampRequestGeneratorExamples {

    @Test
    public void shouldGenerateAndSaveMD5HashedRequest() throws FileNotFoundException, IOException, NoSuchAlgorithmException {
        final URL resource = getClass().getResource("source-file");
        final File file = new File(resource.getFile());

        assertThat(file.exists()).isTrue();

        final FileInputStream input = new FileInputStream(file);
        final byte[] data = IOUtils.toByteArray(input);
        input.close();

        final MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(data);

        final TimeStampRequestGenerator requestGenerator = new TimeStampRequestGenerator();
        final TimeStampRequest request = requestGenerator.generate(TSPAlgorithms.MD5, digest.digest());

        final URL md5Request = getClass().getResource(".");
        final File requestFile =  new File(md5Request.getFile() + "md5.tsr");

        final FileOutputStream output = new FileOutputStream(requestFile);
        IOUtils.write(request.getEncoded(), output);
        output.close();
    }

    @Test
    public void shouldGenerateAndSaveSHA1HashedRequest() throws FileNotFoundException, IOException, NoSuchAlgorithmException {
        final URL resource = getClass().getResource("source-file");
        final File file = new File(resource.getFile());

        assertThat(file.exists()).isTrue();

        final FileInputStream input = new FileInputStream(file);
        final byte[] data = IOUtils.toByteArray(input);
        input.close();

        final MessageDigest digest = MessageDigest.getInstance("SHA1");
        digest.update(data);

        final TimeStampRequestGenerator requestGenerator = new TimeStampRequestGenerator();
        final TimeStampRequest request = requestGenerator.generate(TSPAlgorithms.SHA1, digest.digest());

        final URL md5Request = getClass().getResource(".");
        final File requestFile =  new File(md5Request.getFile() + "sha1.tsr");

        final FileOutputStream output = new FileOutputStream(requestFile);
        IOUtils.write(request.getEncoded(), output);
        output.close();
    }

    @Test
    public void shouldGenerateAndSaveSHA256HashedRequest() throws FileNotFoundException, IOException, NoSuchAlgorithmException {
        final URL resource = getClass().getResource("source-file");
        final File file = new File(resource.getFile());

        assertThat(file.exists()).isTrue();

        final FileInputStream input = new FileInputStream(file);
        final byte[] data = IOUtils.toByteArray(input);
        input.close();

        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(data);

        final TimeStampRequestGenerator requestGenerator = new TimeStampRequestGenerator();
        final TimeStampRequest request = requestGenerator.generate(TSPAlgorithms.SHA256, digest.digest());

        final URL md5Request = getClass().getResource(".");
        final File requestFile =  new File(md5Request.getFile() + "sha256.tsr");

        final FileOutputStream output = new FileOutputStream(requestFile);
        IOUtils.write(request.getEncoded(), output);
        output.close();
    }

}
