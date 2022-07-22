Create table urls (
	id int auto_increment unique,
    title varchar(100),
    description varchar(300),
    url varchar(300) not null,
    conn_time datetime,
    last_check datetime,
    status varchar(40),
    primary key (id)
);
--Insert into URLS (id, url) values(1,'https://google.com/');
