insert into employee (id,department,email,name,password) values('ROOT','ROOT','ROOT@tgfc.tw','ROOT','{bcrypt}$2a$10$WodINMg6UpNWbOSmYgOAseYGlAUXfxwG7DXHiudLfkQBiv2zfRz8a');
insert into employee (id,department,email,name,password) values('TGFC062','管理部','TGFC062@tgfc.tw','林承豊','$10$8Fjrg4I2fSAl/NwHID6NLefh5kqIZM17AZkCid8FIomI/BS1Lw/EO');
insert into book (author,isbn,language,name,page,price,pub_house,publish_date,purchase_date,status,type) values ('聖嚴法師','9576330998','繁體中文','行雲流水','250',200,'法鼓','1993-12-01 00:00:00','2019-09-04 00:00:00',1,'文學小說');
insert into book (author,isbn,language,name,page,price,pub_house,publish_date,purchase_date,status,type) values ('徐惠群','9789578189881','繁體中文','觀光行銷管理實務','250',200,'揚智','1993-12-01 00:00:00','2019-09-04 00:00:00',1,'專業/教科書/政府出版品');
INSERT INTO `recommend` VALUES (1,'Howard', '12345682', '正則表達概論與應用', 'HowHouse', '2019-09-29 13:00:00', '懶得想', 1,null);
INSERT INTO `announcement` VALUES (1,'我是內容', '2019-10-14 09:31:34', 'ROOT', '2019-09-06 08:00:00', '2019-09-05 08:00:00', false , '我是標題','ROOT');
INSERT INTO `records` VALUES (1,'2019-10-14 08:00:00', 'ROOT', 'ROOT', '2019-10-28 08:00:00', null , 1 , 1,'ROOT');
INSERT INTO reservation (id,start_date,end_date,status,book_id,employee_id) VALUES (1, '2019-09-08 13:00:00', '2019-09-15 13:00:00', 1,1,'ROOT');
INSERT INTO reservation (id,start_date,end_date,status,book_id,employee_id) VALUES (2, '2019-09-08 13:00:00', '2019-09-15 13:00:00', 1,1,'TGFC062');
