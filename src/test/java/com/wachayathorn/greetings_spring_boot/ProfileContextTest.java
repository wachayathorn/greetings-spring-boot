package com.wachayathorn.greetings_spring_boot;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

class ProfileContextTest {

    @Test
    void localProfileStartsWithDetailsAndH2Console() {
        assertProfile("local", "true", "true");
    }

    @Test
    void devProfileStartsWithDetailsAndWithoutH2Console() {
        assertProfile("dev", "true", "false");
    }

    @Test
    void stgProfileHidesDetails() {
        assertProfile("stg", "false", "false");
    }

    @Test
    void prodProfileHidesDetails() {
        assertProfile("prod", "false", "false");
    }

    private static void assertProfile(String profile, String includeDetails, String h2Console) {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(GreetingsSpringBootApplication.class)
                .profiles(profile)
                .properties("spring.main.web-application-type=none")
                .run()) {
            assertThat(context.getEnvironment().getActiveProfiles()).contains(profile);
            assertThat(context.getEnvironment().getProperty("app.error.include-details")).isEqualTo(includeDetails);
            assertThat(context.getEnvironment().getProperty("spring.h2.console.enabled")).isEqualTo(h2Console);
        }
    }
}
