INSERT INTO `user` VALUES (1, 'yba.com', '123456', 'ybd', '2019-01-18 18:21:05');
INSERT INTO `user` VALUES (2, 'yangbingdong1.com', '123456', 'ybd', '2019-01-18 18:21:05');
INSERT INTO `user` VALUES (3, 'yangbingdong2.com', '123456', 'ybd1', '2019-01-18 18:21:05');
INSERT INTO `user` VALUES (4, 'yangbingdong3.com', '123456', 'ybd2', '2019-01-18 18:21:05');
INSERT INTO `user` VALUES (5, 'yqy.com', '123456', 'yqy', '2018-10-10 10:10:10');

INSERT INTO `role` VALUES (1, 'master');
INSERT INTO `role` VALUES (2, 'slave');

INSERT INTO `user_role` VALUES (1, 1, 1);
INSERT INTO `user_role` VALUES (2, 1, 2);
INSERT INTO `user_role` VALUES (3, 2, 2);