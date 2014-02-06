package com.github.fzilic.tsp.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;

public final class PkcsOidHelper {

    private PkcsOidHelper() {
        // util
    }

    private static final Map<ASN1ObjectIdentifier, String> oids = new HashMap<ASN1ObjectIdentifier, String>();

    private static void init() throws Exception {
        final Field[] fields = PKCSObjectIdentifiers.class.getDeclaredFields();

        for (final Field field : fields) {
            if (ASN1ObjectIdentifier.class.equals(field.getType()) && Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
                final String name = field.getName();
                final ASN1ObjectIdentifier oid = (ASN1ObjectIdentifier) field.get(null);

                oids.put(oid, name);
            }
        }

    }

    public static String nameForOid(final String p_oid) {
        if (oids.isEmpty()) {
            try {
                init();
            } catch (final Exception exception) {
                return null;
            }
        }

        return oids.get(new ASN1ObjectIdentifier(p_oid));
    }

}
