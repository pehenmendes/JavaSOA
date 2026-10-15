-- Hash de senha não deve ser único: a unicidade correta é a do login.
alter table usuarios drop index senha;

-- Colunas com default não devem aceitar NULL.
update usuarios set perfil = 'USER' where perfil is null;
update usuarios set ativo = 1 where ativo is null;
update alunos set ativo = 1 where ativo is null;
update instrutores set ativo = 1 where ativo is null;
update instrucoes set ativo = 1 where ativo is null;

alter table usuarios modify perfil varchar(20) not null default 'USER';
alter table usuarios modify ativo tinyint not null default 1;
alter table alunos modify ativo tinyint not null default 1;
alter table instrutores modify ativo tinyint not null default 1;
alter table instrucoes modify ativo tinyint not null default 1;

-- Índices para as consultas de conflito de agenda.
create index idx_instrucoes_instrutor_data_hora on instrucoes (instrutor_id, data_hora);
create index idx_instrucoes_aluno_data_hora on instrucoes (aluno_id, data_hora);
