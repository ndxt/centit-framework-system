
create sequence S_ROLECODE;


update F_ROLEINFO
set ROLE_CODE = substr(ROLE_CODE, 3)
where ROLE_CODE like 'G-%';

update F_ROLEPOWER
set ROLE_CODE = substr(ROLE_CODE, 3)
where ROLE_CODE like 'G-%';

update F_USERROLE
set ROLE_CODE = substr(ROLE_CODE, 3)
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
    b.ROLE_DESC, b.CREATE_DATE, b.UPDATE_DATE ,a.USER_CODE, null as INHERITED_FROM
    from F_USERROLE a join F_ROLEINFO b on (a.ROLE_CODE=b.ROLE_CODE)
    where a.OBTAIN_DATE <=  sysdate and (a.SECEDE_DATE is null or a.SECEDE_DATE > sysdate) and b.IS_VALID='T'
union
  select b.ROLE_CODE, b.ROLE_NAME, b.IS_VALID, 'I' as OBTAIN_TYPE, b.ROLE_TYPE, b.UNIT_CODE,
    b.ROLE_DESC, b.CREATE_DATE, b.UPDATE_DATE ,c.USER_CODE, a.UNIT_CODE as INHERITED_FROM
    from F_UNITROLE a join F_ROLEINFO b on (a.ROLE_CODE = b.ROLE_CODE) JOIN F_USERUNIT c on( a.UNIT_CODE = c.UNIT_CODE)
    where a.OBTAIN_DATE <=  sysdate and (a.SECEDE_DATE is null or a.SECEDE_DATE > sysdate) and b.IS_VALID='T'
