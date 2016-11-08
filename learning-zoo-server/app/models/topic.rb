class Topic < ActiveRecord::Base
  belongs_to :sessions, inverse_of: :topics
end
