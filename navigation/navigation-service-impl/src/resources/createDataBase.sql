create if not exists database Ilona;

create user ilona identified by 'ilona';

grant all on Ilona.* to 'ilona';
grant all on Ilona.* to 'ilona'@'localhost' identified by 'ilona' with grant option;