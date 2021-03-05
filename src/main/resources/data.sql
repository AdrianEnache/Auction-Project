INSERT INTO ROLE (id,name ) values (1,"ROLE_ADMIN");
INSERT INTO ROLE (id,name ) values (2,"ROLE_PARTICIPANT");

-- create table `persistent_logins` (
--                                      `username` varchar(64) COLLATE utf8_unicode_ci not null,
--                                      `series` varchar(64) COLLATE utf8_unicode_ci not null,
--                                      `token` varchar(64) COLLATE utf8_unicode_ci not null,
--                                      `last_used` timestamp not null DEFAULT CURRENT_TIMESTAMP,
--                                      primary key (`series`)
-- )ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;