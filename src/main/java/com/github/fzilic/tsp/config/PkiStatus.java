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

public enum PkiStatus {
    GRANTED(0), GRANTED_WITH_MODS(1), REJECTION(2), WAITING(3), REVOCATION_WARNING(4), REVOCATION_NOTIFICATION(5), KEY_UPDATE_WARNING(6);

    private final int m_status;

    private PkiStatus(final int p_status) {
        m_status = p_status;
    }

    public static final boolean isGranted(final int p_status) {
        final PkiStatus status = forStatus(p_status);
        if (status == null) {
            return false;
        }

        if (GRANTED.equals(status) || GRANTED_WITH_MODS.equals(status)) {
            return true;
        }

        return false;
    }

    public static final PkiStatus forStatus(final int p_status) {
        for (final PkiStatus status : PkiStatus.values()) {
            if (status.m_status == p_status) {
                return status;
            }
        }

        return null;
    }
}
