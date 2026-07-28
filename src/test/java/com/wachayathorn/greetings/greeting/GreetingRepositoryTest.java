package com.wachayathorn.greetings.greeting;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class GreetingRepositoryTest {

    @Autowired
    private GreetingRepository greetingRepository;

    @Test
    void save_shouldGenerateIdAndCreatedAt() {
        GreetingEntity saved = greetingRepository.save(new GreetingEntity("Alice"));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Alice");
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void findById_shouldReturnSavedEntity() {
        GreetingEntity saved = greetingRepository.save(new GreetingEntity("Bob"));

        assertThat(greetingRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void deleteById_shouldRemoveEntity() {
        GreetingEntity saved = greetingRepository.save(new GreetingEntity("Carol"));
        Long id = saved.getId();

        greetingRepository.deleteById(id);

        assertThat(greetingRepository.findById(id)).isEmpty();
    }
}
