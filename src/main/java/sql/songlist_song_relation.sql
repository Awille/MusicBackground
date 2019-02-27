/*
 Navicat Premium Data Transfer

 Source Server         : Awille
 Source Server Type    : MySQL
 Source Server Version : 100138
 Source Host           : localhost:3306
 Source Schema         : music

 Target Server Type    : MySQL
 Target Server Version : 100138
 File Encoding         : 65001

 Date: 26/02/2019 16:42:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for songlist_song_relation
-- ----------------------------
DROP TABLE IF EXISTS `songlist_song_relation`;
CREATE TABLE `songlist_song_relation`  (
  `userId` int(11) NOT NULL,
  `songId` int(11) NOT NULL,
  UNIQUE INDEX `relation`(`userId`, `songId`) USING BTREE,
  INDEX `song`(`songId`) USING BTREE,
  CONSTRAINT `song` FOREIGN KEY (`songId`) REFERENCES `song` (`songId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
