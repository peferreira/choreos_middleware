#
# Cookbook Name:: supermarket1
# Recipe:: default
#
# Copyright 2011, YOUR_COMPANY_NAME
#
# All rights reserved - Do Not Redistribute
#
include_recipe 'petals'

DOWNLOAD_SERVER = 'http://valinhos.ime.usp.br:54080/demo/supermarket1'
PETALS_DIR = node['petals']['dir']
DIR2SAVE = PETALS_DIR[/\/opt\/.*\//][0..-2] # first dir below /opt

def download(filename)
  diskFile = "/tmp/#{filename}"
  webFile = "#{DOWNLOAD_SERVER}/#{filename}"

  remote_file diskFile do
    source webFile
    action :create_if_missing
  end
end

# Install components
components = %w{
  sa-BPEL-SM1-provide.zip
  sa-SOAP-SM1-provide.zip
  sa-SOAP-SM1-consume.zip
}

components.each do |filename|
  download filename
  execute 'install component' do
    command "cp /tmp/#{filename} #{PETALS_DIR}/install"
    creates "#{PETALS_DIR}/installed/#{filename}"
    action :run
  end
end

# Install webapp
filename = 'runSM1.jar'
download filename
execute 'save webapp' do
  command "cp /tmp/#{filename} #{DIR2SAVE}/"
  creates "#{DIR2SAVE}/#{filename}"
  action :run
end

# Runs webapp
service "supermarket1_ws" do
  supports :start => true
  start_command "java -jar #{DIR2SAVE}/runSM1.jar 4321 &"
  action [ :start ]
end
