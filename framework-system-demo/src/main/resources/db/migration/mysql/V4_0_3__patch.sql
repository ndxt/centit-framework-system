
/*==============================================================*/
/* Table: F_USERROLE                                            */
/*==============================================================*/
create table F_UNITROLE
(
   UNIT_CODE            varchar(32) not null,
   ROLE_CODE            varchar(32) not null,
   OBTAIN_DATE          datetime not null,
   SECEDE_DATE          datetime,
   CHANGE_DESC          varchar(256),
   update_Date          datetime,
   Create_Date          datetime,
   creator              varchar(32),
   updator              varchar(32)
);

alter table F_UNITROLE
   add primary key (UNIT_CODE, ROLE_CODE);

/*==============================================================*/
/* View: F_V_USERROLES                                          */
/*==============================================================*/
create or replace view F_V_USERROLES as
select b.ROLE_CODE, b.ROLE_NAME, b.IS_VALID, 'D' as OBTAIN_TYPE,
      b.ROLE_DESC, b.CREATE_DATE, b.UPDATE_DATE ,a.USER_CODE, NULL as INHERITED_FROM
    from F_USERROLE a join F_ROLEINFO b on (a.ROLE_CODE=b.ROLE_CODE)
    where a.OBTAIN_DATE <=  now() and (a.SECEDE_DATE is null or a.SECEDE_DATE > now()) and b.IS_VALID='T'
union
  select b.ROLE_CODE, b.ROLE_NAME, b.IS_VALID, 'I' as OBTAIN_TYPE,
        b.ROLE_DESC, b.CREATE_DATE, b.UPDATE_DATE ,c.USER_CODE, a.UNIT_CODE as INHERITED_FROM
    from F_UNITROLE a join F_ROLEINFO b on (a.ROLE_CODE = b.ROLE_CODE) JOIN F_USERUNIT c on( a.UNIT_CODE = c.UNIT_CODE)
    where a.OBTAIN_DATE <=  now() and (a.SECEDE_DATE is null or a.SECEDE_DATE > now()) and b.IS_VALID='T'


