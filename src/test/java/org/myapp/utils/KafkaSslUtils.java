package org.myapp.utils;

import nl.altindag.ssl.SSLFactory;
import nl.altindag.ssl.pem.util.PemUtils;
import org.apache.kafka.common.security.auth.SslEngineFactory;

import javax.net.ssl.SSLEngine;
import java.io.IOException;
import java.nio.file.Path;
import java.security.KeyStore;
import java.util.Map;
import java.util.Set;

public class KafkaSslUtils implements SslEngineFactory {

    @Override
    public SSLEngine createClientSslEngine(String s, int i, String s1) {
        final SSLEngine sslEngine = SSLFactory.builder()
//                .withUnsafeHostnameVerifier() //https site
//                .withUnsafeTrustMaterial()  //don't need pem
                .withTrustMaterial(PemUtils.loadTrustMaterial(
                        Path.of("src/test/resources/ca.pem")))
                .withIdentityMaterial(PemUtils.loadIdentityMaterial(
                        Path.of("src/test/resources/tls.crt"),
                        Path.of("src/test/resources/tls.key")))
                .build()
                .getSslContext()
                .createSSLEngine(s, i);
        sslEngine.setUseClientMode(true);
        return sslEngine;
    }

    @Override
    public SSLEngine createServerSslEngine(String s, int i) {
        return null;
    }

    @Override
    public boolean shouldBeRebuilt(Map<String, Object> map) {
        return false;
    }

    @Override
    public Set<String> reconfigurableConfigs() {
        return Set.of();
    }

    @Override
    public KeyStore keystore() {
        return null;
    }

    @Override
    public KeyStore truststore() {
        return null;
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
