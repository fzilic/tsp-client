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
 */

package com.github.fzilic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;

import com.github.fzilic.config.Algorithm;
import com.github.fzilic.exceptions.DigestException;

public class DigestGenerator {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    private final String     m_filePath;

    private final Algorithm  m_algorithm;

    private MessageDigest    m_messageDigest;

    private byte[]           m_digest            = null;

    private File             m_data;

    public DigestGenerator(final String p_filePath, final Algorithm p_algorithm) {
        m_algorithm = p_algorithm;
        m_filePath = p_filePath;
    }

    public byte[] digest() {
        if (m_digest == null) {
            m_digest = m_messageDigest.digest();
        }
        return m_digest;
    }

    public DigestGenerator generateDigest() {
        if (m_messageDigest == null || m_data == null) {
            throw new DigestException("Not initialised correctly");
        }

        InputStream input = null;

        try {
            input = new FileInputStream(m_data);
        } catch (final FileNotFoundException exception) {
            throw new DigestException("Failed to open input stream.", exception);
        }

        final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int read = 0;
        try {
            while ((read = input.read(buffer)) != -1) {
                m_messageDigest.update(buffer, 0, read);
            }
        } catch (final IOException exception) {
            throw new DigestException("Failed to read file", exception);
        }

        try {
            input.close();
        } catch (final IOException exception) {
            throw new DigestException("Failed to close input stream", exception);
        }

        return this;
    }

    public DigestGenerator initDigest() {
        try {
            m_messageDigest = MessageDigest.getInstance(m_algorithm.getDigestAlgorithm());
        } catch (final NoSuchAlgorithmException exception) {
            throw new DigestException("Failed to get algorithm instance for " + m_algorithm.getDigestAlgorithm(), exception);
        }
        return this;
    }

    public DigestGenerator initFile() {
        m_data = new File(m_filePath);
        if (!m_data.exists() || !m_data.isFile() || !m_data.canRead()) {
            throw new DigestException("File does not exist, is not a file, or is not readable");
        }
        return this;
    }

    public DigestGenerator storeDigest(final String p_digestFile) {
        if (m_digest == null) {
            m_digest = m_messageDigest.digest();
        }
        final File digestFile = new File(p_digestFile);

        try {
            final OutputStream output = new FileOutputStream(digestFile);
            IOUtils.write(String.format("%s  %s", new String(Hex.encodeHex(m_digest)), m_data.getName()), output);
            output.close();
        } catch (final IOException exception) {
            throw new DigestException("Failed to save digest to file: " + digestFile.getName(), exception);
        }

        return this;
    }
}
