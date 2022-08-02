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
            System.setProperty("statussvcdb", container.getJdbcUrl());
            System.setProperty("root", container.getUsername());
            System.setProperty("password", container.getPassword());
        }

        @Override
        public void stop() {
            //do nothing, JVM handles shut down
        }
    }
