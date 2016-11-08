class Subject < ActiveRecord::Base
  has_many :users, inverse_of: :subjects
  has_many :sessions, inverse_of: :subjects

  validates :name, presence: true
end
