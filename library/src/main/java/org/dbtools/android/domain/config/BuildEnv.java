package org.dbtools.android.domain.config;

public enum BuildEnv {
    MAVEN("target/test-db/"),
    GRADLE("build/test-db/");

    private String testDbDir;

    BuildEnv(String testDbDir) {
        this.testDbDir = testDbDir;
    }

    public String getTestDbDir() {
        return testDbDir;
    }
}
