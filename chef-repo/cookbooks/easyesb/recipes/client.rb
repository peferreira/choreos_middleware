#
# Cookbook Name:: easyesb
# Recipe:: client
#
# Downloads EasyESB client
#
# Copyright 2011, USP
#
# LGPL 2.0 or, at your option, any later version
#

include_recipe "apt" # java recipe is failing without recipe apt
include_recipe "java"

remote_file "#{node['easyesb']['cli']['downloaded_file']}" do
  source "#{node['easyesb']['cli']['url']}"
  action :create_if_missing
end

execute 'extract_easyesb_client' do
  command "tar -zxf #{node['easyesb']['cli']['downloaded_file']}"
  creates "#{node['easyesb']['cli']['executable_jar']}"
  cwd "#{node['easyesb']['cli']['work_dir']}"
  action :run
end

