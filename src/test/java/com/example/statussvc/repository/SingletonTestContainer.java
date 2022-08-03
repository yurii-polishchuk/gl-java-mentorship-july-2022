package com.example.statussvc.repository;

import org.testcontainers.containers.MySQLContainer;

public class SingletonTestContainer extends MySQLContainer<SingletonTestContainer> {
        private static final String IMAGE_VERSION = "mysql:8.0";
        private static SingletonTestContainer container;

        private SingletonTestContainer() {
            super(IMAGE_VERSION);
        }

        public static SingletonTestContainer getInstance() {
            if (container == null) {
                container = new SingletonTestContainer();
            }
            return container;
        }

        @Override
        public void start() {
            super.start();
            System.setProperty("DB_URL", container.getJdbcUrl());
            System.setProperty("DB_USERNAME", container.getUsername());
            System.setProperty("DB_PASSWORD", container.getPassword());
        }

        @Override
        public void stop() {
            //do nothing, JVM handles shut down
        }
    }
