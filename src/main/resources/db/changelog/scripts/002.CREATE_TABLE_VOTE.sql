create table vote (
  election_id integer not null,
  voter_id integer not null,
  candidate_id integer not null,
  constraint pk_ev primary key (election_id, voter_id)
);
