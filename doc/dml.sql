/*
-- Query: SELECT * FROM zhb_vue.function_info
LIMIT 0, 1000

-- Date: 2018-09-14 14:26
*/
INSERT INTO `function_info` (`id`,`name`,`type`,`path`,`order_index`,`icon_id`,`parent_id`,`delete_flag`) VALUES ('4kw9jvjidehmhndy','图标管理',0,'iconinfocontroller',21,'hnw3rhtilwfmagdw',NULL,0);
INSERT INTO `function_info` (`id`,`name`,`type`,`path`,`order_index`,`icon_id`,`parent_id`,`delete_flag`) VALUES ('8k7ffuhvrp892n78','用户信息',1,'/htgl/userinfocontroller/toindex',2,NULL,'u6pb294pa2uk1xj7',0);
INSERT INTO `function_info` (`id`,`name`,`type`,`path`,`order_index`,`icon_id`,`parent_id`,`delete_flag`) VALUES ('egnfukjiioldmeut','图标信息',1,'/htgl/iconinfocontroller/toindex',22,NULL,'4kw9jvjidehmhndy',0);
INSERT INTO `function_info` (`id`,`name`,`type`,`path`,`order_index`,`icon_id`,`parent_id`,`delete_flag`) VALUES ('sehur4jilth8lsr7','功能管理',0,'functioninfocontroller',11,'tdwhrvjilthmhsdl',NULL,0);
INSERT INTO `function_info` (`id`,`name`,`type`,`path`,`order_index`,`icon_id`,`parent_id`,`delete_flag`) VALUES ('sp9mdx3sn9rgxslt','授权管理',0,'authoritycontroller',31,'azxt39kmmgb5bcj5',NULL,0);
INSERT INTO `function_info` (`id`,`name`,`type`,`path`,`order_index`,`icon_id`,`parent_id`,`delete_flag`) VALUES ('ttnhwujilklmedbl','功能信息',1,'/htgl/functioninfocontroller/toindex',12,NULL,'sehur4jilth8lsr7',0);
INSERT INTO `function_info` (`id`,`name`,`type`,`path`,`order_index`,`icon_id`,`parent_id`,`delete_flag`) VALUES ('u6pb294pa2uk1xj7','用户管理',0,'userinfocontroller',1,'w5wexvq1ltmrhckg',NULL,0);
INSERT INTO `function_info` (`id`,`name`,`type`,`path`,`order_index`,`icon_id`,`parent_id`,`delete_flag`) VALUES ('yynj13mymz785b12','授权信息',1,'/htgl/authoritycontroller/toindex',32,NULL,'sp9mdx3sn9rgxslt',0);


/*
-- Query: SELECT * FROM zhb_vue.icon_info
LIMIT 0, 1000

-- Date: 2018-09-14 14:27
*/
INSERT INTO `icon_info` (`id`,`name`,`value`,`delete_flag`,`create_time`,`update_time`) VALUES ('azxt39kmmgb5bcj5','授权管理','logo-designernews',0,'2018-09-13 16:36:06','2018-09-13 16:36:24');
INSERT INTO `icon_info` (`id`,`name`,`value`,`delete_flag`,`create_time`,`update_time`) VALUES ('hnw3rhtilwfmagdw','图标管理','ios-information-circle-outline',0,'2018-09-13 16:36:28','2018-09-13 16:36:28');
INSERT INTO `icon_info` (`id`,`name`,`value`,`delete_flag`,`create_time`,`update_time`) VALUES ('tdwhrvjilthmhsdl','功能管理','ios-construct',0,'2018-09-13 16:36:32','2018-09-13 16:36:32');
INSERT INTO `icon_info` (`id`,`name`,`value`,`delete_flag`,`create_time`,`update_time`) VALUES ('w5wexvq1ltmrhckg','用户管理','ios-person',0,'2018-09-13 16:36:34','2018-09-13 16:37:49');


alter table dic_info modify code varchar(10);
