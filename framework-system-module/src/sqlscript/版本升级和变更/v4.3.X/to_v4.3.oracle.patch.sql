-- 添加顶级机构 用于 帐套管理
alter table F_USERINFO add TOP_UNIT varchar2(32);


CREATE OR REPLACE VIEW v_hi_unitinfo AS
SELECT a.unit_code AS top_unit_code, b.unit_code, b.unit_type, b.parent_unit, b.is_valid, b.unit_name,b.unit_desc,b.unit_short_name, b.unit_order, b.dep_no,
       b.unit_word,b.unit_grade,
       LENGTH(b.Unit_Path)- LENGTH(REPLACE(b.Unit_Path,'/','')) - LENGTH(a.Unit_Path) + LENGTH(REPLACE(a.Unit_Path,'/',''))+1  AS hi_level,
       substr(b.Unit_Path ,  LENGTH(a.Unit_Path)+1) AS Unit_Path
  FROM F_UNITINFO a , F_UNITINFO b
 WHERE b.Unit_Path LIKE CONCAT(a.Unit_Path,'%' );