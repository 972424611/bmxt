CREATE DATABASE bmxt DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

create table tb_athlete
(
  id int(10) unsigned auto_increment
    primary key,
  name varchar(64) default '' not null comment '运动员姓名',
  team varchar(64) default '' not null comment '代表队',
  gender int default '0' not null comment '0:未知 1:男 2:女',
  event varchar(64) default '' not null comment '比赛报名项目',
  birthday varchar(64) default '' not null comment '生日',
  number varchar(64) not null comment '身份证号',
  photo_name varchar(16) null,
  create_time datetime not null comment '创建时间',
  update_time datetime not null comment '修改时间'
)
;

create table tb_item
(
  id int(10) unsigned auto_increment
    primary key,
  name varchar(128) not null comment '比赛名称',
  number int default '1' not null comment '每条划艇人数
			单人-1, 双人-2, 四人-4',
  conditions varchar(128) null comment '该比赛限制要求',
  max_boats int not null comment '最大艇数',
  event varchar(64) not null comment '大项',
  match_id int not null comment '关联的match表中字段',
  create_time datetime null comment '创建时间'
)
  comment '细分的比赛项目'
;

create table tb_match
(
  id int(10) unsigned auto_increment
    primary key,
  name varchar(128) not null comment '赛事名称',
  event varchar(64) not null comment '比赛项目',
  host varchar(64) default '' not null comment '举办地',
  start_time datetime not null comment '开始时间',
  end_time datetime not null comment '结束时间',
  status int(10) unsigned default '0' null comment '报名状态 1-正在进行 0-关闭',
  create_time datetime null comment '创建时间'
)
  comment '赛事'
;

create table tb_match_item_athlete
(
  match_id int not null,
  item_id int not null,
  athlete_message varchar(64) not null,
  boat_id int not null comment '第几艇',
  team varchar(12) not null comment '代表队'
)
;

create table tb_user
(
  id int(10) unsigned auto_increment
    primary key,
  username varchar(64) not null comment '各省份约定俗称的英文名',
  password varchar(128) not null comment '密码',
  province varchar(64) default '' not null comment 'username对应的中文省份名称',
  constraint tb_user_username_uindex
  unique (username),
  constraint tb_user_provinces_uindex
  unique (province)
)
  comment '账户'
;