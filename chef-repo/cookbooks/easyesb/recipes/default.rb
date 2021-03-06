#
# Cookbook Name:: easyesb
# Recipe:: default
#
# Downloads and starts EasyESB
#
# Copyright 2011, USP
#
# LGPL 2.0 or, at your option, any later version
#

include_recipe "apt" # java recipe is failing without recipe apt
include_recipe "java"

remote_file "#{node['easyesb']['downloaded_file']}" do
  source "#{node['easyesb']['url']}"
  action :create_if_missing
end

execute 'extract_easyesb' do
  command "tar -zxf #{node['easyesb']['downloaded_file']}"
  creates "#{node['easyesb']['executable_jar']}"
  cwd "#{node['easyesb']['work_dir']}"
  action :run
end

service "start_easyesb" do
  start_command "cd #{node['easyesb']['bin_folder']} ; java -jar #{node['easyesb']['jar_name']} &"
  action [ :start ]
end

script "wait_easyesb_start" do
  interpreter "bash"
  code <<-EOH
  echo "Waiting for EasyESB start by monitoring its log: #{node['easyesb']['log_file']}"
  message='<sequence>18</sequence>'
  while ! cat #{node['easyesb']['log_file']} | grep -q "$message"
  do
    sleep 1
  done
  echo 'EasyESB has started'
  EOH
  action :run
end


