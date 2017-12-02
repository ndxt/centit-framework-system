
INSERT INTO f_mysql_sequence (name, currvalue , increment) VALUES
  ('S_ROLECODE', 0, 1);


update F_ROLEINFO
set ROLE_CODE = substring(ROLE_CODE, 3)
where ROLE_CODE like 'G-%';

update F_ROLEPOWER
set ROLE_CODE = substring(ROLE_CODE, 3)
where ROLE_CODE like 'G-%';

update F_USERROLE
set ROLE_CODE = substring(ROLE_CODE, 3)
where ROLE_CODE like 'G-%';

update F_ROLEINFO
set ROLE_TYPE = 'G';

update F_ROLEINFO
set ROLE_TYPE = 'F'
where ROLE_CODE = 'public' or ROLE_CODE = 'anonymous' or ROLE_CODE = 'forbidden';

/*==============================================================*/
/* View: F_V_USERROLES                                          */
/*==============================================================*/
create or replace view F_V_USERROLES as
select b.ROLE_CODE, b.ROLE_NAME, b.IS_VALID, 'D' as OBTAIN_TYPE, b.ROLE_TYPE, b.UNIT_CODE,
      b.ROLE_DESC, b.CREATE_DATE, b.UPDATE_DATE ,a.USER_CODE, NULL as INHERITED_FROM
    from F_USERROLE a join F_ROLEINFO b on (a.ROLE_CODE=b.ROLE_CODE)
    where a.OBTAIN_DATE <=  now() and (a.SECEDE_DATE is null or a.SECEDE_DATE > now()) and b.IS_VALID='T'
union
  select b.ROLE_CODE, b.ROLE_NAME, b.IS_VALID, 'I' as OBTAIN_TYPE, b.ROLE_TYPE, b.UNIT_CODE,
        b.ROLE_DESC, b.CREATE_DATE, b.UPDATE_DATE ,c.USER_CODE, a.UNIT_CODE as INHERITED_FROM
    from F_UNITROLE a join F_ROLEINFO b on (a.ROLE_CODE = b.ROLE_CODE) JOIN F_USERUNIT c on( a.UNIT_CODE = c.UNIT_CODE)
    where a.OBTAIN_DATE <=  now() and (a.SECEDE_DATE is null or a.SECEDE_DATE > now()) and b.IS_VALID='T';


insert into F_ROLEINFO (ROLE_CODE, ROLE_NAME,ROLE_TYPE, IS_VALID, ROLE_DESC, CREATE_DATE, UPDATE_DATE,CREATOR,UPDATOR)
values ('forbidden', '禁用的功能','F', 'T', '这个角色不能赋给任何人，这个角色中的操作任何人都不可以调用。', str_to_date('12-12-2014 16:05:46', '%d-%m-%Y %H:%i:%s'), now(),'u0000000','u0000000');

