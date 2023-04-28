package com.tlcsdm.core.util

import static org.junit.jupiter.api.DynamicTest.dynamicTest

import org.junit.jupiter.api.*
import org.junit.jupiter.api.condition.DisabledIfSystemProperty
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.junit.platform.launcher.listeners.LoggingListener

import java.util.logging.ConsoleHandler
import java.util.logging.Level
import java.util.logging.Logger
import java.util.stream.Stream

@DisabledIfSystemProperty(named = "env", matches = "workflow", disabledReason = "Not support in github action")
class MyTest {

    @BeforeAll
    static void init() {
        def logger = Logger.getLogger(LoggingListener.name)
        logger.level = Level.FINE
        logger.addHandler(new ConsoleHandler(level: Level.FINE))
    }

    @Test
    void streamSum() {
        Assertions.assertTrue(Stream.of(1, 2, 3)
                .count() > 2)
    }

    @RepeatedTest(value = 2, name = "{displayName} {currentRepetition}/{totalRepetitions}")
    void streamSumRepeated() {
        assert Stream.of(1, 2, 3).count() == 3
    }

    private boolean isPalindrome(s) {
        s == s.reverse()
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "racecar",
        "radar",
        "able was I ere I saw elba"
    ])
    void palindromes(String candidate) {
        assert isPalindrome(candidate)
    }

    @TestFactory
    def dynamicTestCollection() {
        [
            dynamicTest("Add test",  {assert 1 + 1 == 2}),
            dynamicTest("Multiply Test",{assert 2 * 3 == 6})
        ]
    }
}
