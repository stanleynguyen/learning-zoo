class Session < ActiveRecord::Base
  has_many :topics, inverse_of: :sessions
  belongs_to :subjects, inverse_of: :sessions

  validates :name, :lecturer_name, :current_slide, :subject, presence: true
end
