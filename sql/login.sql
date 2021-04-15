-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: login
-- ------------------------------------------------------
-- Server version	5.7.11-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `game_notice_list`
--

DROP TABLE IF EXISTS `game_notice_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `game_notice_list` (
  `notice_id` int(11) NOT NULL DEFAULT '1',
  `content` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game_notice_list`
--

LOCK TABLES `game_notice_list` WRITE;
/*!40000 ALTER TABLE `game_notice_list` DISABLE KEYS */;
INSERT INTO `game_notice_list` VALUES (1,'222222222222222222222222222222222222222222222222222222222222222222222'),(2,'333333333333333333333333333333333333333');
/*!40000 ALTER TABLE `game_notice_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `game_player_info`
--

DROP TABLE IF EXISTS `game_player_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `game_player_info` (
  `open_id` varchar(30) NOT NULL,
  `player_index` int(11) NOT NULL AUTO_INCREMENT,
  `server_info` varchar(255) NOT NULL,
  PRIMARY KEY (`open_id`),
  UNIQUE KEY `player_index_UNIQUE` (`player_index`)
) ENGINE=InnoDB AUTO_INCREMENT=100000 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game_player_info`
--

LOCK TABLES `game_player_info` WRITE;
/*!40000 ALTER TABLE `game_player_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `game_player_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `game_player_server_info`
--

DROP TABLE IF EXISTS `game_player_server_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `game_player_server_info` (
  `id` int(11) NOT NULL,
  `player_index` int(11) DEFAULT NULL,
  `server_id` smallint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game_player_server_info`
--

LOCK TABLES `game_player_server_info` WRITE;
/*!40000 ALTER TABLE `game_player_server_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `game_player_server_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `game_server_list`
--

DROP TABLE IF EXISTS `game_server_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `game_server_list` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `server_name` varchar(45) DEFAULT 'null' COMMENT '服务器名称',
  `server_id` int(11) DEFAULT '0' COMMENT '服务器id',
  `state` tinyint(4) DEFAULT '0' COMMENT '服务器状态 0 开启 1 维护',
  `server_ip` varchar(45) DEFAULT NULL,
  `open_time` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `server_id_UNIQUE` (`server_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100000 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game_server_list`
--

LOCK TABLES `game_server_list` WRITE;
/*!40000 ALTER TABLE `game_server_list` DISABLE KEYS */;
INSERT INTO `game_server_list` VALUES (7,'测试1',6,0,'127.0.0.1',1618153469);
/*!40000 ALTER TABLE `game_server_list` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-04-16  0:57:26
