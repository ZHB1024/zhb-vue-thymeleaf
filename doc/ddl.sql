
/*用户表 */
create table user_info
(
  id                 VARCHAR(16) not null,
  user_name			 VARCHAR(15) not null,
  password           VARCHAR(100) not null,
  salt				 VARCHAR(8) not null,
  real_name          VARCHAR(20) ,
  sex				 VARCHAR(2) ,	
  birthday           DATETIME not null,
  identity_card		 VARCHAR(18) ,
  country			 VARCHAR(50) ,
  nation			 VARCHAR(50) ,
  
  byyx				 VARCHAR(50) ,
  
  mobile_phone       VARCHAR(11),
  email              VARCHAR(30) ,
  
  lob_id			 VARCHAR(16) ,
  
  create_time         DATETIME not null,
  update_time         DATETIME,
  delete_flag             int(1) not null,
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*功能表 */
create table function_info
(
  id                 VARCHAR(16) not null,
  name			     VARCHAR(20) not null,
  type				 smallint(1) not null,	
  path				 VARCHAR(100) not null,
  order_index 			 int(2) not null,	
  icon_id			 VARCHAR(16) ,
  parent_id			 VARCHAR(16) ,
  delete_flag             int(1) not null,
  create_user_id	 VARCHAR(16) not null,
  create_time         DATETIME not null,
  update_time         DATETIME,
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*用户-功能表 */
create table user_function_info
(
  id                 VARCHAR(16) not null,
  user_id			 VARCHAR(16) not null,
  function_id	     VARCHAR(16) not null,
  create_time         DATETIME not null,
  update_time         DATETIME,
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;



/*图标表 */
create table icon_info
(
  id                 VARCHAR(16) not null,
  name			     VARCHAR(20) not null,
  value              VARCHAR(50) not null,
  delete_flag             int(1) not null,
  create_user_id	 VARCHAR(16) not null,
  create_time         DATETIME not null,
  update_time         DATETIME,
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;



/*字典表 */
create table dic_info
(
  id                 VARCHAR(16) not null,
  category           VARCHAR(10) not null,
  code           VARCHAR(5) not null,
  name           VARCHAR(50) not null,
  name2           VARCHAR(50) ,
  name3           VARCHAR(50) ,
  type         VARCHAR(1) not null,  
  order_index        int(5) not null,  
  remark           VARCHAR(50),
  delete_flag             int(1) not null,
  create_user_id	 VARCHAR(16) not null,
  create_time         DATETIME not null,
  update_time         DATETIME,
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*验证码表 */
create table verification_code_info
(
  id                 VARCHAR(16) not null,
  email           VARCHAR(50) ,
  mobile_phone           VARCHAR(11) ,
  type         int not null,  
  code           VARCHAR(10) not null,
  remark           VARCHAR(50),
  delete_flag             int not null,
  create_time         DATETIME not null,
  update_time         DATETIME ,
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*附件表 */
create table attachment_info
(
  id                 VARCHAR(16) not null,
  file_name           VARCHAR(50) not null,
  file_size           VARCHAR(10) not null,
  content_type         VARCHAR(200) not null,  
  file_path           VARCHAR(100) not null,
  thumbnail_path           VARCHAR(100) ,
  type           	int not null,  
  delete_flag             int not null,
  create_user_id	 VARCHAR(16) not null,
  create_time         DATETIME not null,
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*新增user_id */
alter table attachment_info add CREATE_USER_ID	VARCHAR(16);
alter table dic_info add CREATE_USER_ID	VARCHAR(16);
alter table icon_info add CREATE_USER_ID	VARCHAR(16);
alter table function_info add CREATE_USER_ID	VARCHAR(16);

alter table dic_info add create_time	DATETIME;
alter table user_function_info add create_time	DATETIME;

alter table function_info add upate_time	DATETIME;

