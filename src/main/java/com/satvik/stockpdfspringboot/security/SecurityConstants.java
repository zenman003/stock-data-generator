package com.satvik.stockpdfspringboot.security;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 864000;
    public static final Key SECRET_KEY = new SecretKeySpec("masterYoda".getBytes(), "AES");

}
