 create table if not exists host (
    id bigint not null,
    title varchar(100) not null,
    description varchar(300),
    url varchar(300) not null,
    connection_time int not null,
    last_check datetime(6) not null,
    status varchar(15) not null,
    primary key (id)
) engine=InnoDB;
