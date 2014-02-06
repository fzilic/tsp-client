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

import org.kohsuke.args4j.Option;

public class Configuration {

    @Option(name = "-d", aliases = { "--data" }, usage = "Path to file containing data")
    private String    m_data;

    @Option(name = "-g", aliases = { "--digest" }, usage = "Store digest to this file")
    private String    m_digestFile;

    @Option(name = "-q", aliases = { "--query" }, usage = "Timestamp Query file (.tsq) path. If not set it will not be stored.")
    private String    m_queryFile;

    @Option(name = "-r", aliases = { "--resp" }, usage = "Timestamp Response file (.tsr) path. If not set it will not be stored.")
    private String    m_responseFile;

    @Option(name = "-c", aliases = { "--cert-req" }, usage = "Set certReq in RFC3161 request to TRUE.")
    private boolean   m_certReq;

    @Option(name = "-u", aliases = { "--url" }, usage = "TSA URL for used to request Timestamp token. If not set it will not be used")
    private String    m_url;

    @Option(name = "-v", aliases = { "--verbosity" }, usage = "Prints verbose output.")
    private Verbosity m_verbosity;

    @Option(name = "-a", aliases = { "--alg", "--algorithm" }, usage = "TSP digest algorithm [MD5, SHA1, SHA256]")
    private Algorithm m_algorithm = Algorithm.SHA1;

    @Option(name = "-h", aliases = { "--help", "-H" }, usage = "Prints help")
    private boolean   m_helpAsked;

    public Algorithm getAlgorithm() {
        return m_algorithm;
    }

    public String getData() {
        return m_data;
    }

    public String getDigestFile() {
        return m_digestFile;
    }

    public String getQueryFile() {
        return m_queryFile;
    }

    public String getResponseFile() {
        return m_responseFile;
    }

    public String getUrl() {
        return m_url;
    }

    public Verbosity getVerbosity() {
        return m_verbosity;
    }

    public boolean isCertReq() {
        return m_certReq;
    }

    public boolean isHelpAsked() {
        return m_helpAsked;
    }

    public boolean isVerbose() {
        return m_verbosity != null;
    }

    public void setAlgorithm(final Algorithm p_algorithm) {
        m_algorithm = p_algorithm;
    }

    public void setCertReq(final boolean p_certReq) {
        m_certReq = p_certReq;
    }

    public void setData(final String p_data) {
        m_data = p_data;
    }

    public void setDigestFile(final String p_digestFile) {
        m_digestFile = p_digestFile;
    }

    public void setHelpAsked(final boolean p_helpAsked) {
        m_helpAsked = p_helpAsked;
    }

    public void setQueryFile(final String p_queryFile) {
        m_queryFile = p_queryFile;
    }

    public void setResponseFile(final String p_responseFile) {
        m_responseFile = p_responseFile;
    }

    public void setUrl(final String p_url) {
        m_url = p_url;
    }

    public void setVerbosity(final Verbosity p_verbosity) {
        m_verbosity = p_verbosity;
    }

}
