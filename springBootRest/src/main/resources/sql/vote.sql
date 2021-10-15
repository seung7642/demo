-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema vote
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema vote
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `vote` DEFAULT CHARACTER SET utf8 ;
USE `vote` ;

-- -----------------------------------------------------
-- Table `vote`.`key_generator`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vote`.`key_generator` (
  `idx` INT NOT NULL,
  `cnt` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`idx`))
ENGINE = InnoDB;

INSERT INTO key_generator (idx, cnt) VALUES ('0', '0');
INSERT INTO key_generator (idx, cnt) VALUES ('1', '0');
INSERT INTO key_generator (idx, cnt) VALUES ('2', '0');
INSERT INTO key_generator (idx, cnt) VALUES ('3', '0');
INSERT INTO key_generator (idx, cnt) VALUES ('4', '0');
INSERT INTO key_generator (idx, cnt) VALUES ('5', '0');
INSERT INTO key_generator (idx, cnt) VALUES ('6', '0');
INSERT INTO key_generator (idx, cnt) VALUES ('7', '0');
INSERT INTO key_generator (idx, cnt) VALUES ('8', '0');
INSERT INTO key_generator (idx, cnt) VALUES ('9', '0');


-- -----------------------------------------------------
-- Table `vote`.`vote`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vote`.`vote` (
 `vote_id` CHAR(10) CHARACTER SET 'utf8' NOT NULL,
 `article_id` BIGINT NOT NULL,
 `user_id` CHAR(4) CHARACTER SET 'utf8' NOT NULL,
 `title` CHAR(100) CHARACTER SET 'utf8' NOT NULL,
 `description` TEXT NULL DEFAULT NULL,
 `deadline` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 `total_cnt` INT NOT NULL DEFAULT '0',
 PRIMARY KEY (`vote_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `vote`.`vote_item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vote`.`vote_item` (
  `item_id` BIGINT NOT NULL AUTO_INCREMENT,
  `vote_id` CHAR(10) CHARACTER SET 'utf8' NOT NULL,
  `item` CHAR(50) CHARACTER SET 'utf8' NOT NULL,
  `cnt` INT NOT NULL DEFAULT '0',
  PRIMARY KEY (`item_id`),
  INDEX `fk_item_vote_idx` (`vote_id` ASC) VISIBLE,
  CONSTRAINT `fk_item_vote`
      FOREIGN KEY (`vote_id`)
          REFERENCES `vote`.`vote` (`vote_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `vote`.`vote_item_detail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vote`.`vote_item_detail` (
 `item_id` BIGINT NOT NULL,
 `user_id` CHAR(4) CHARACTER SET 'utf8' NOT NULL,
 INDEX `fk_detail_item_idx` (`item_id` ASC) VISIBLE,
 PRIMARY KEY (`item_id`, `user_id`),
 CONSTRAINT `fk_detail_item`
     FOREIGN KEY (`item_id`)
         REFERENCES `vote`.`vote_item` (`item_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


CREATE USER IF NOT EXISTS 'test'@'localhost' IDENTIFIED BY 'test';
GRANT ALL PRIVILEGES ON vote.* TO 'test'@'localhost';


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
