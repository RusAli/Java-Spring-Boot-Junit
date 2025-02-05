package org.myapp.tests;

import io.restassured.RestAssured;
import lombok.SneakyThrows;
import nl.altindag.ssl.SSLFactory;
import nl.altindag.ssl.pem.util.PemUtils;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.net.ssl.*;
import java.io.IOException;
import java.nio.file.Path;
import java.security.cert.X509Certificate;

import static io.restassured.RestAssured.given;

public class RestAssuredWithSsl {

    @BeforeAll
    @SneakyThrows
    public static void setUp(){

        SSLFactory sslFactory = SSLFactory.builder()
//                .withUnsafeHostnameVerifier() //https site
//                .withUnsafeTrustMaterial()  //don't need pem
                .withTrustMaterial(PemUtils.loadTrustMaterial(
                        Path.of("src/test/resources/ca.pem")))
                .withIdentityMaterial(PemUtils.loadIdentityMaterial(
                        Path.of("src/test/resources/tls.crt"),
                        Path.of("src/test/resources/tls.key")))
                .build();

        SSLContext sslContext = sslFactory.getSslContext();
        DeprecatedAdapterVerify adapterVerify = new DeprecatedAdapterVerify(sslFactory.getHostnameVerifier());

        // Создаем Deprecated объекты из httpclient4, на которых работает RestAssures
        final org.apache.http.conn.ssl.SSLSocketFactory socketFactory =
                new org.apache.http.conn.ssl.SSLSocketFactory(sslContext, adapterVerify);

        // Создаем новый sslConfig для RestAssured
        final io.restassured.config.SSLConfig sslConfig = new io.restassured.config.SSLConfig();
        sslConfig.sslSocketFactory(socketFactory);

        // Устанавливаем новый sslConfig
        final io.restassured.config.RestAssuredConfig newConfig = io.restassured.config.RestAssuredConfig.config();
        newConfig.sslConfig(sslConfig);
        io.restassured.RestAssured.config = newConfig;
    }

    @Test
    void restAssuredTest() {
        given().get();
    }

    /**
     * Класс нужен для адаптации к Deprecated api httpclient4
     */
    @SuppressWarnings("deprecation")
    static class DeprecatedAdapterVerify implements X509HostnameVerifier {
        private final HostnameVerifier verifier;

        public static X509HostnameVerifier adapt(HostnameVerifier verifier) {
            if (verifier instanceof X509HostnameVerifier) return (X509HostnameVerifier) verifier;
            return new DeprecatedAdapterVerify(verifier);
        }

        private DeprecatedAdapterVerify(HostnameVerifier verifier) {
            this.verifier = verifier;
        }

        public boolean verify(String hostname, SSLSession session) {
            return this.verifier.verify(hostname, session);
        }

        public void verify(String host, SSLSocket socket) throws IOException {
            if (!this.verify(host, socket.getSession())) {
                throw new SSLException("<" + host + "> does not pass hostname verification");
            }
        }

        public void verify(String host, X509Certificate cert) {
            throw new UnsupportedOperationException();
        }

        public void verify(String host, String[] cns, String[] subjectAlts) {
            throw new UnsupportedOperationException();
        }
    }
}
