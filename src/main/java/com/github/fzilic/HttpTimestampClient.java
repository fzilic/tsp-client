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

package com.github.fzilic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.Certificate;


import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampResponse;

import com.github.fzilic.exceptions.ClientException;

public class HttpTimestampClient {

    private final HttpPost         m_post;

    private final TimeStampRequest m_request;

    private StatusLine             m_statusLine;

    private ByteArrayInputStream   m_responseContent;

    private TimeStampResponse      m_response;

    public HttpTimestampClient(final String p_url, final TimeStampRequest p_timeStampRequest) {
        m_post = new HttpPost(p_url);
        m_post.addHeader("User-Agent", "CROZ TSA Client");
        m_request = p_timeStampRequest;
    }

    public HttpTimestampClient basicAuth(final String p_username, final String p_password) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public HttpTimestampClient execute() {
        final DefaultHttpClient client = new DefaultHttpClient();
        try {
            final HttpResponse httpResponse = client.execute(m_post);
            m_statusLine = httpResponse.getStatusLine();

            m_responseContent = new ByteArrayInputStream(IOUtils.toByteArray(httpResponse.getEntity().getContent()));
        } catch (final IOException exception) {
            throw new ClientException("Failed to get response from server", exception);
        } finally {
            m_post.releaseConnection();
        }
        return this;
    }

    public int httpStatus() {
        return m_statusLine.getStatusCode();
    }

    public HttpTimestampClient initClient() {
        final ContentType contentType = ContentType.create("application/timestamp-query");

        try {
            final HttpEntity entity = new ByteArrayEntity(m_request.getEncoded(), contentType);
            m_post.setEntity(entity);
        } catch (final IOException exception) {
            throw new ClientException("Failed to create post entity", exception);
        }

        return this;
    }

    public TimeStampResponse timestampResponse() {
        try {
            if (m_response == null) {
                m_response = new TimeStampResponse(m_responseContent);
            }

            return m_response;
        } catch (final TSPException exception) {
            throw new ClientException("Failed to read timestamp response from http response", exception);
        } catch (final IOException exception) {
            throw new ClientException("Failed to read timestamp response from http response", exception);
        }
    }

    public HttpTimestampClient unsafeSsl() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public HttpTimestampClient x509Auth(final Certificate p_certificate) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
