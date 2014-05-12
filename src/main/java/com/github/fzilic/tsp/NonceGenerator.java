package com.github.fzilic.tsp;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.github.fzilic.tsp.exceptions.ClientException;

public final class NonceGenerator {

    public static final BigInteger generateNonce() {
        try {
            final SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            return new BigInteger(64, random);
        } catch (final NoSuchAlgorithmException exception) {
            throw new ClientException("Failed to initialize SecureRandom instance", exception);
        }
    }
}
