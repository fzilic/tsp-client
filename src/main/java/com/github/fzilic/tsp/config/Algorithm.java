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
package com.github.fzilic.tsp.config;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.tsp.TSPAlgorithms;

public enum Algorithm {
    MD5(TSPAlgorithms.MD5, "MD5"), SHA1(TSPAlgorithms.SHA1, "SHA1"), SHA256(TSPAlgorithms.SHA256, "SHA-256");

    private final ASN1ObjectIdentifier m_tspAlgorithm;

    private final String               m_digestAlgorithm;

    private Algorithm(final ASN1ObjectIdentifier p_tspAlgorithm, final String p_digestAlgorithm) {
        m_tspAlgorithm = p_tspAlgorithm;
        m_digestAlgorithm = p_digestAlgorithm;
    }

    public String getDigestAlgorithm() {
        return m_digestAlgorithm;
    }

    public ASN1ObjectIdentifier getTspAlgorithm() {
        return m_tspAlgorithm;
    }

    public static final String hashAlgorithmForASN1ObjectIdentifier(final ASN1ObjectIdentifier p_asn1ObjectIdentifier) {
        for (final Algorithm alg : Algorithm.values()) {
            if (alg.getTspAlgorithm().equals(p_asn1ObjectIdentifier)) {
                return alg.name();
            }
        }
        return null;
    }

    public static final String hashAlgorithmForASN1ObjectIdentifier(final String p_asn1ObjectIdentifier) {
        final ASN1ObjectIdentifier objectIdentifier = new ASN1ObjectIdentifier(p_asn1ObjectIdentifier);

        for (final Algorithm alg : Algorithm.values()) {
            if (alg.getTspAlgorithm().equals(objectIdentifier)) {
                return alg.name();
            }
        }
        return null;
    }

}
