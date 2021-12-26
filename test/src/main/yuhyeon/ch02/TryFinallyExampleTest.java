package main.yuhyeon.ch02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import yuhyeon.ch02.item09.TryFinallyExample;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TryFinallyExampleTest {

    @Test
    @DisplayName("Suppressed_테스트")
    void Suppressed_테스트() {
        assertThatThrownBy(() -> TryFinallyExample.firstLineOfFileTryResources("testPath", "a"))
                .isInstanceOf(NullPointerException.class);

    }
}
