create table election_voter (
  election_id integer not null,
  voter_id integer not null,
  constraint pk_ev primary key (election_id, voter_id),
  constraint fk_ev foreign key (election_id) references election
);
