create table users (
  username                varchar(100) primary key,
  account_non_locked      boolean      not null,
  account_non_expired     boolean      not null,
  authorities             varchar(100) not null,
  credentials_non_expired boolean      not null,
  enabled                 boolean      not null,
  password                varchar(100) not null
);
