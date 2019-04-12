create table IF NOT EXISTS sqlmap (
    gubun_ varchar(200) not null,
    key_ varchar(200) not null,
    sql_ varchar(400) not null,
    PRIMARY KEY(gubun_, key_)
);