 create table host (
    id bigint not null,
    title varchar(100) not null,
    description varchar(1024),
    url varchar(1024) not null,
    connection_time int,
    last_check timestamp,
    status varchar(15) not null,
    primary key (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_general_ci;
