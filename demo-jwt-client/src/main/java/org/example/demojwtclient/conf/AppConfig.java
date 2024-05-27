package org.example.demojwtclient.conf;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

@Configuration
public class AppConfig {

    //    @Bean
    //    public RestTemplate restTemplate() {
    //        return new RestTemplate();
    //    }
    @Value("${trust.store}")
    private Resource trustStore;

    @Value("${trust.store.password}")
    private String trustStorePassword;

    @Bean
    public RestTemplate restTemplate() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, MalformedURLException, IOException {
        // Konfiguracja TrustManager, który zaakceptuje wszystkie certyfikaty
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        SSLConnectionSocketFactory sslConFactory = new SSLConnectionSocketFactory(sslContext);

        HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslConFactory)
                .build();

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        return new RestTemplate(requestFactory);
    }
}
