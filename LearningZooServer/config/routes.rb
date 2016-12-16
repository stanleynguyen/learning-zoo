Rails.application.routes.draw do
  mount_devise_token_auth_for 'User', at: 'auth'
  root "home#index"

  resources :topics do
    get :incr_counter, on: :member
  end

  resources :sessions do
    get :get_topics, on: :member
  end

  resources :subjects
  resources :classrooms
end
