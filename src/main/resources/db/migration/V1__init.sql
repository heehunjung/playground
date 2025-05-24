create table article
(
    id      bigint       not null auto_increment,
    title   varchar(255) not null,
    content varchar(255) not null,
    primary key (id)
);

create table member
(
    id        bigint       not null auto_increment,
    nick_name varchar(255) not null,
    email     varchar(255),
    oauth     varchar(255) not null,
    primary key (id)
);

create table token
(
    id            bigint       not null auto_increment,
    refresh_token varchar(255) not null,
    member_id     bigint       not null,
    primary key (id),
    unique (member_id)
);

alter table token
    add constraint FK_token_member
        foreign key (member_id) references member (id);
