package org.keycloak.example.spring;

import net.rossillo.spring.web.mvc.CacheControlHandlerInterceptor;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;


/*@SpringBootApplication(exclude = {EmbeddedServletContainerAutoConfiguration.class,
                                  WebMvcAutoConfiguration.class})*/
@SpringBootApplication
public class DatabaseService extends SpringBootServletInitializer {

    /**
     * Initializes this application when running as a standalone application.
     */
    public static void main(String[] args)  {
/*        new SpringApplicationBuilder()
            .bannerMode(Banner.Mode.OFF)
            .sources(DatabaseService.class)
            .run(args);*/
              SpringApplication.run(DatabaseService.class, args);

    }

    /**
     * Initializes this application when running in a servlet container (e.g. Tomcat)
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DatabaseService.class);
    }

  @Bean
  public CacheControlHandlerInterceptor cacheControlHandlerInterceptor() {
    return new CacheControlHandlerInterceptor();
  }



}
