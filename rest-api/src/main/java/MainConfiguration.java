import com.codahale.metrics.httpclient.HttpClientMetricNameStrategy;
import com.fasterxml.jackson.annotation.JsonProperty;
import extra.ExternalServceConfig;
import io.dropwizard.Configuration;
import io.dropwizard.client.HttpClientConfiguration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by rishabh.agarwal on 23/05/15.
 */
public class MainConfiguration extends Configuration {
    @Valid
    @NotNull
    private DataSourceFactory dataSourceFactory;
    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    @Valid
    @NotNull
    private HttpClientConfiguration httpClientConfiguration;

    @JsonProperty("httpClient")
    public HttpClientConfiguration getHttpClientConfiguration() {
        return httpClientConfiguration;
    }

    @JsonProperty("httpClient")
    public void setHttpClientConfiguration(HttpClientConfiguration httpClientConfiguration) {
        this.httpClientConfiguration = httpClientConfiguration;
    }

    @Valid
    @NotNull
    private ExternalServceConfig externalServceConfig;

    @JsonProperty("externalService")
    public ExternalServceConfig getExternalServceConfig() {
        return externalServceConfig;
    }

    @JsonProperty("externalService")
    public void setExternalServceConfig(ExternalServceConfig externalServceConfig) {
        this.externalServceConfig = externalServceConfig;
    }


}
