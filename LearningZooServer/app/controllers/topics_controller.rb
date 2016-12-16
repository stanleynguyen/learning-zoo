class TopicsController < ApplicationController
  before_action :prepare_topic, only: [:show, :update, :destroy, :incr_counter]

  def index
    @topics = Topic.all
    render json: @topics
  end

  def show
    render json: @topic
  end

  def create
    @topic = Topic.new(topic_params)
    if @topic.save
      render json: @topic
    else
      render json: { errors: @topic.errors.full_messages }
    end
  end

  def update
    if @topic.update(topic_params)
      render json: @topic
    else
      render json: { errors: @topic.errors.full_messages }
    end
  end

  def destroy
    @topic.destroy
    render json: { success: "true" }
  rescue ActiveRecord::InvalidForeignKey
    render json: { errors: "this topic is in use by other things" }
  end

  def incr_counter
    @topic.std_unclear += 1
    render json: @topic
  end

  private

  def prepare_topic
    @topic = Topic.find(params[:id])
  end

  def topic_params
    params.require(:topic).permit(:name, :start_slide, :end_slide, :std_unclear, :session_id, :machine_key,:time)
  end

end
