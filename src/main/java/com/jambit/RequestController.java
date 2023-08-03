package com.jambit;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Part;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.ProxyHttpClient;
import io.micronaut.http.client.multipart.MultipartBody;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import java.net.URI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reactivestreams.Publisher;

/**
 * The controller class using a {@link ProxyHttpClient} to forward the request to the given target.
 */
@Controller
@Requires(property = "meta.api.target-url")
@Requires(property = "meta.api.access-token")
@ExecuteOn(TaskExecutors.BLOCKING)
public class RequestController {

    private static final Logger LOG = LogManager.getLogger(RequestController.class);

    private final ProxyHttpClient proxyHttpClient;
    private final URI targetUri;
    private final String accessToken;

    public RequestController(
            final ProxyHttpClient proxyHttpClient,
            @Property(name = "meta.api.target-url") final URI targetUri,
            @Property(name= "meta.api.access-token") final String accessToken
    ) {
        this.proxyHttpClient = proxyHttpClient;
        this.targetUri = targetUri;
        this.accessToken = accessToken;
    }

    @Post(consumes = MediaType.MULTIPART_FORM_DATA)
    public Publisher<MutableHttpResponse<?>> proxy(
            @Part final String data,
            @Nullable @Part(value = "test_event_code") final String testEventCode,
            @Header final String userAgent,
            @Nullable @Header final String trueClientIp,
            final HttpRequest<?> originRequest
    ) {
        final String enrichedData = FormdataEnrichmentService.getEnrichedData(data, userAgent, trueClientIp);

        LOG.debug("received data part:\r\n{}", data);
        LOG.debug("Got user agent: {}", userAgent);
        LOG.debug("Got user address: {}", trueClientIp);
        LOG.debug("Received test event code: {}", testEventCode);
        LOG.debug("Enriched data part:\r\n{}", enrichedData);
        if (LOG.isTraceEnabled()) {
            originRequest.getAttributes().forEach((attribute, value) -> LOG.trace("[Attribute] {}: {}", attribute, value));
            originRequest.getHeaders().forEach((header, value) -> LOG.trace("[Header] {}: {}", header, value));
        }

        LOG.error(Thread.currentThread().toString());
        final Publisher<MutableHttpResponse<?>> response = proxyHttpClient.proxy(originRequest.mutate()
                .uri(targetUri)
                .body(
                        createMultipartBodyBuilder(enrichedData, testEventCode)
                )
        );
        return response;
    }

    private MultipartBody.Builder createMultipartBodyBuilder(
            final String enrichedData,
            final String testEventCode
    ) {
        final MultipartBody.Builder builder =
                MultipartBody.builder()
                        .addPart("data", enrichedData)
                        .addPart("access_token", accessToken);
        return (testEventCode != null) ? builder.addPart("test_event_code", testEventCode) : builder;
    }
}
