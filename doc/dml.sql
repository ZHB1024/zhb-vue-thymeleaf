/*
-- Query: SELECT * FROM zhb_vue.function_info
LIMIT 0, 1000

-- Date: 2018-09-14 14:26
*/
INSERT INTO `function_info` (`id`,`name`,`type`,`path`,`order_index`,`icon_id`,`parent_id`,`delete_flag`,`create_user_id`,`create_time`) VALUES ('4kw9jvjidehmhndy','图标管理',0,'iconinfocontroller',21,'hnw3rhtilwfmagdw',NULL,0,'60styuiuzzj5fyir','2019-06-08 23:36:10');
INSERT INTO `function_info` (`id`,`name`,`type`,`path`,`order_index`,`icon_id`,`parent_id`,`delete_flag`,`create_user_id`,`create_time`) VALUES ('8k7ffuhvrp892n78','用户信息',1,'/htgl/userinfocontroller/toindex',2,NULL,'u6pb294pa2uk1xj7',0,'60styuiuzzj5fyir','2019-06-08 23:36:42');
INSERT INTO `function_info` (`id`,`name`,`type`,`path`,`order_index`,`icon_id`,`parent_id`,`delete_flag`,`create_user_id`,`create_time`) VALUES ('egnfukjiioldmeut','图标信息',1,'/htgl/iconinfocontroller/toindex',22,NULL,'4kw9jvjidehmhndy',0,'60styuiuzzj5fyir','2019-06-08 23:36:50');
INSERT INTO `function_info` (`id`,`name`,`type`,`path`,`order_index`,`icon_id`,`parent_id`,`delete_flag`,`create_user_id`,`create_time`) VALUES ('sehur4jilth8lsr7','功能管理',0,'functioninfocontroller',11,'tdwhrvjilthmhsdl',NULL,0,'60styuiuzzj5fyir','2019-06-08 23:36:30');
INSERT INTO `function_info` (`id`,`name`,`type`,`path`,`order_index`,`icon_id`,`parent_id`,`delete_flag`,`create_user_id`,`create_time`) VALUES ('sp9mdx3sn9rgxslt','授权管理',0,'authoritycontroller',31,'azxt39kmmgb5bcj5',NULL,0,'60styuiuzzj5fyir','2019-06-08 23:36:50');
INSERT INTO `function_info` (`id`,`name`,`type`,`path`,`order_index`,`icon_id`,`parent_id`,`delete_flag`,`create_user_id`,`create_time`) VALUES ('ttnhwujilklmedbl','功能信息',1,'/htgl/functioninfocontroller/toindex',12,NULL,'sehur4jilth8lsr7',0,'60styuiuzzj5fyir','2019-06-08 23:36:30');
INSERT INTO `function_info` (`id`,`name`,`type`,`path`,`order_index`,`icon_id`,`parent_id`,`delete_flag`,`create_user_id`,`create_time`) VALUES ('u6pb294pa2uk1xj7','用户管理',0,'userinfocontroller',1,'w5wexvq1ltmrhckg',NULL,0,'60styuiuzzj5fyir','2019-06-08 23:36:20');
INSERT INTO `function_info` (`id`,`name`,`type`,`path`,`order_index`,`icon_id`,`parent_id`,`delete_flag`,`create_user_id`,`create_time`) VALUES ('yynj13mymz785b12','授权信息',1,'/htgl/authoritycontroller/toindex',32,NULL,'sp9mdx3sn9rgxslt',0,'60styuiuzzj5fyir','2019-06-08 23:36:40');


/*
-- Query: SELECT * FROM zhb_vue.icon_info
LIMIT 0, 1000

-- Date: 2018-09-14 14:27
*/
INSERT INTO `icon_info` (`id`,`name`,`value`,`delete_flag`,'create_user_id',`create_time`,`update_time`) VALUES ('azxt39kmmgb5bcj5','授权管理','logo-designernews',0,'60styuiuzzj5fyir','2018-09-13 16:36:06','2018-09-13 16:36:24');
INSERT INTO `icon_info` (`id`,`name`,`value`,`delete_flag`,'create_user_id',`create_time`,`update_time`) VALUES ('hnw3rhtilwfmagdw','图标管理','ios-information-circle-outline',0,'60styuiuzzj5fyir','2018-09-13 16:36:28','2018-09-13 16:36:28');
INSERT INTO `icon_info` (`id`,`name`,`value`,`delete_flag`,'create_user_id',`create_time`,`update_time`) VALUES ('tdwhrvjilthmhsdl','功能管理','ios-construct',0,'60styuiuzzj5fyir','2018-09-13 16:36:32','2018-09-13 16:36:32');
INSERT INTO `icon_info` (`id`,`name`,`value`,`delete_flag`,'create_user_id',`create_time`,`update_time`) VALUES ('w5wexvq1ltmrhckg','用户管理','ios-person',0,'60styuiuzzj5fyir','2018-09-13 16:36:34','2018-09-13 16:37:49');



INSERT INTO `user_function_info` (`id`,`user_id`,`function_id`,`create_time`) VALUES ('xb8ex8u1lkmrlckd','60styuiuzzj5fyir','ttnhwujilklmedbl','2018-09-13 16:37:49');


