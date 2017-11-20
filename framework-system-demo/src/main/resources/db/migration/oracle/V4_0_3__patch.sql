
create table F_UNITROLE
(
   UNIT_CODE            varchar2(32) not null,
   ROLE_CODE            varchar2(32) not null,
   OBTAIN_DATE          date not null,
   SECEDE_DATE          date,
   CHANGE_DESC          varchar2(256),
   update_Date          date,
   Create_Date          date,
   creator              varchar2(32),
   updator              varchar2(32)
);

alter table F_UNITROLE add primary key (UNIT_CODE, ROLE_CODE);

create or replace view F_V_USERROLES as
select b.ROLE_CODE, b.ROLE_NAME, b.IS_VALID, 'D' as OBTAIN_TYPE,
       b.ROLE_DESC, b.CREATE_DATE, b.UPDATE_DATE ,a.USER_CODE, null as inheritedFrom
    from F_USERROLE a join F_ROLEINFO b on (a.ROLE_CODE=b.ROLE_CODE)
    where a.OBTAIN_DATE <=  sysdate and (a.SECEDE_DATE is null or a.SECEDE_DATE > sysdate) and b.IS_VALID='T'
union
  select b.ROLE_CODE, b.ROLE_NAME, b.IS_VALID, 'I' as OBTAIN_TYPE,
      b.ROLE_DESC, b.CREATE_DATE, b.UPDATE_DATE ,c.USER_CODE, a.UNIT_CODE as inheritedFrom
    from F_UNITROLE a join F_ROLEINFO b on (a.ROLE_CODE = b.ROLE_CODE) JOIN F_USERUNIT c on( a.UNIT_CODE = c.UNIT_CODE)
    where a.OBTAIN_DATE <=  sysdate and (a.SECEDE_DATE is null or a.SECEDE_DATE > sysdate) and b.IS_VALID='T'
