CREATE TABLE host
(
    id              bigint        NOT NULL AUTO_INCREMENT,
    title           varchar(128)  NOT NULL,
    description     varchar(1024) NOT NULL,
    url             varchar(1024) NOT NULL,
    connection_time int           NOT NULL DEFAULT 0,
    last_check      timestamp              DEFAULT NULL,
    status          varchar(16)   NOT NULL,
    primary key (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;
